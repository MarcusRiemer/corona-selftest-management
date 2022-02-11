import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, firstValueFrom } from 'rxjs';

export interface LoginState {
  isLoggedIn: boolean;
  roles: Set<string>;
}

const ROLE_ANONYMOUS = 'ROLE_ANONYMOUS';

/**
 * A very barebones wrapper around the "traditional" SpringBoot
 * form based login. This seemed to be the easiest way to get
 * started with the authentication / authorisation process.
 */
@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly _isLoggedIn$ = new BehaviorSubject(false);

  public readonly isLoggedIn$ = this._isLoggedIn$.asObservable();

  constructor(private readonly http: HttpClient) {}

  get isLoggedIn() {
    return this._isLoggedIn$.value;
  }

  public roles = new Set<string>();

  /**
   * Attempts to login with the given credentials
   */
  async login(username: string, password: string): Promise<LoginState> {
    const data = new FormData();
    data.set('username', username);
    data.set('password', password);

    try {
      // TODO: Fetch roles on login
      // TODO: Don't rely on non-success exception
      await firstValueFrom(this.http.post<boolean>('/api/session/login', data));
      this._isLoggedIn$.next(true);
    } catch (e) {
      this.resetStateLoggedOut();
    }

    if (this.isLoggedIn) {
      this.roles = await this.fetchCurrentRoles();
    }

    return { isLoggedIn: this.isLoggedIn, roles: this.roles };
  }

  /**
   * Checks what the server knows about our session.
   */
  async fetchLoginState(): Promise<LoginState> {
    const response = await this.fetchCurrentRoles();
    this.roles = new Set<string>(response);

    if (this.roles.size === 1 && this.roles.has(ROLE_ANONYMOUS)) {
      this.resetStateLoggedOut();
    } else {
      this._isLoggedIn$.next(true);
    }

    return { isLoggedIn: this.isLoggedIn, roles: this.roles };
  }

  async logout() {
    try {
      await firstValueFrom(this.http.post('/api/session/logout', {}));
    } finally {
      this.resetStateLoggedOut();
    }

    return { isLoggedIn: this.isLoggedIn, roles: this.roles };
  }

  hasRole(roleName: string) {
    return this.roles.has(roleName);
  }

  private async fetchCurrentRoles(): Promise<Set<string>> {
    try {
      return new Set<string>(
        await firstValueFrom(this.http.get<string[]>('/api/session/roles'))
      );
    } catch (e) {
      return new Set<string>();
    }
  }

  private resetStateLoggedOut() {
    this._isLoggedIn$.next(false);
    this.roles = new Set<string>();
  }
}

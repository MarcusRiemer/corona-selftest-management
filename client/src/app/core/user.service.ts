import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

export interface LoginState {
  isLoggedIn: boolean;
  roles: string[];
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
  constructor(private readonly http: HttpClient) {}

  public isLoggedIn = false;

  public roles: string[] = [];

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
      this.isLoggedIn = true;
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
    this.roles = await this.fetchCurrentRoles();

    if (this.roles.length === 1 && this.roles[0] === ROLE_ANONYMOUS) {
      this.resetStateLoggedOut();
    } else {
      this.isLoggedIn = true;
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

  private async fetchCurrentRoles(): Promise<string[]> {
    try {
      return await firstValueFrom(
        this.http.get<string[]>('/api/session/roles')
      );
    } catch (e) {
      return [];
    }
  }

  private resetStateLoggedOut() {
    this.isLoggedIn = false;
    this.roles = [];
  }
}

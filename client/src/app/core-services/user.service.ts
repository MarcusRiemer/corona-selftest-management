import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private readonly http: HttpClient) {}

  public isLoggedIn = false;

  async login(username: string, password: string): Promise<boolean> {
    const data = new FormData();
    data.set('username', username);
    data.set('password', password);

    this.isLoggedIn = await firstValueFrom(
      this.http.post<boolean>('/api/session/login', data)
    );

    return this.isLoggedIn;
  }
}

import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { firstValueFrom } from 'rxjs';
import { LoginCredentialsComponent } from '../login-credentials/login-credentials.component';
import { LoginState, UserService } from '../user.service';

@Component({
  selector: 'app-login-state',
  templateUrl: './login-state.component.html',
  styleUrls: ['./login-state.component.scss'],
})
export class LoginStateComponent implements OnInit {
  state: LoginState = {
    isLoggedIn: false,
    roles: [],
  };

  constructor(
    private readonly userService: UserService,
    public dialog: MatDialog
  ) {}
  async ngOnInit() {
    this.state = await this.userService.fetchLoginState();
  }

  async login() {
    //this.state = await this.userService.login('admin', 'pass');

    const dialogRef = this.dialog.open(LoginCredentialsComponent);
    const res = await firstValueFrom(dialogRef.afterClosed());

    console.log('Login Credentials Result: ', res);

    if (res) {
      this.state = res;
    }
  }

  async logout() {
    this.state = await this.userService.logout();
  }
}

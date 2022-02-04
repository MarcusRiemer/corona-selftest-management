import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../user.service';

@Component({
  selector: 'app-login-credentials',
  templateUrl: './login-credentials.component.html',
  styleUrls: ['./login-credentials.component.scss'],
})
export class LoginCredentialsComponent {
  constructor(
    private readonly userService: UserService,
    private readonly dialogRef: MatDialogRef<LoginCredentialsComponent>
  ) {}

  username = '';
  password = '';

  hasError = false;

  async login() {
    const res = await this.userService.login(this.username, this.password);
    if (res.isLoggedIn) {
      this.dialogRef.close(res);
    } else {
      this.hasError = true;
    }
  }
}

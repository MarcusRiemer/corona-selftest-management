import { Component } from '@angular/core';
import { UserService } from './core/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  constructor(private readonly userService: UserService) {}
  showFiller = false;

  hasRole(role: string) {
    return this.userService.hasRole(role);
  }
}

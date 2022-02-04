import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule } from '@angular/material/dialog';

import { CoreRoutingModule } from './core-routing.module';
import { AboutComponent } from './about/about.component';
import { LoginCredentialsComponent } from './login-credentials/login-credentials.component';
import { LoginStateComponent } from './login-state/login-state.component';
import { FormsModule } from '@angular/forms';

const MATERIAL_MODULES = [MatDialogModule];

@NgModule({
  declarations: [
    AboutComponent,
    LoginCredentialsComponent,
    LoginStateComponent,
  ],
  exports: [LoginStateComponent],
  imports: [CommonModule, FormsModule, CoreRoutingModule, ...MATERIAL_MODULES],
})
export class CoreModule {}

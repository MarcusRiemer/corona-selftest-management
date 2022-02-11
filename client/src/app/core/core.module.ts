import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

import { CoreRoutingModule } from './core-routing.module';
import { AboutComponent } from './about/about.component';
import { LoginCredentialsComponent } from './login-credentials/login-credentials.component';
import { LoginStateComponent } from './login-state/login-state.component';
import { FormsModule } from '@angular/forms';
import { SelectDateComponent } from './select-date/select-date.component';

const MATERIAL_MODULES = [
  MatDialogModule,
  MatDatepickerModule,
  MatNativeDateModule,
];

@NgModule({
  declarations: [
    AboutComponent,
    LoginCredentialsComponent,
    LoginStateComponent,
    SelectDateComponent,
  ],
  exports: [LoginStateComponent, SelectDateComponent],
  imports: [CommonModule, FormsModule, CoreRoutingModule, ...MATERIAL_MODULES],
})
export class CoreModule {}

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { AdministrationRoutingModule } from './administration-routing.module';
import { AdministrationComponent } from './administration.component';

@NgModule({
  declarations: [AdministrationComponent],
  imports: [CommonModule, FormsModule, AdministrationRoutingModule],
})
export class AdministrationModule {}

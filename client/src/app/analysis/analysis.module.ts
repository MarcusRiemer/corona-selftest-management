import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';

import { AnalysisRoutingModule } from './analysis-routing.module';
import { IndexComponent } from './index/index.component';
import { MatNativeDateModule } from '@angular/material/core';

const ANGULAR_MATERIAL_MODULES = [
  MatDatepickerModule,
  MatFormFieldModule,
  MatNativeDateModule,
];

@NgModule({
  declarations: [IndexComponent],
  imports: [CommonModule, AnalysisRoutingModule, ...ANGULAR_MATERIAL_MODULES],
})
export class AnalysisModule {}

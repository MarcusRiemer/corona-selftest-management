import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';

import { AnalysisRoutingModule } from './analysis-routing.module';
import { IndexComponent } from './index/index.component';
import { MatNativeDateModule } from '@angular/material/core';
import { DashboardComponent } from './dashboard/dashboard.component';

const ANGULAR_MATERIAL_MODULES = [
  MatDatepickerModule,
  MatFormFieldModule,
  MatNativeDateModule,
  MatCardModule,
  MatTableModule,
];

@NgModule({
  declarations: [IndexComponent, DashboardComponent],
  imports: [
    CommonModule,
    FormsModule,
    AnalysisRoutingModule,
    ...ANGULAR_MATERIAL_MODULES,
  ],
})
export class AnalysisModule {}

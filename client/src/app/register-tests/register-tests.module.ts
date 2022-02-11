import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';

import { RegisterTestsRoutingModule } from './register-tests-routing.module';
import { IndexComponent } from './index/index.component';
import { PeopleGroupListComponent } from './people-group-list/people-group-list.component';
import { RegisterForPeopleGroupComponent } from './register-for-people-group/register-for-people-group.component';
import { SelectTestOriginComponent } from './select-test-origin/select-test-origin.component';
import { SelectTestResultComponent } from './select-test-result/select-test-result.component';
import { ExemptionStatusComponent } from './exemption-status/exemption-status.component';

const ANGULAR_MATERIAL_MODULES = [
  MatCardModule,
  MatDividerModule,
  MatRadioModule,
  MatIconModule,
];

@NgModule({
  declarations: [
    IndexComponent,
    PeopleGroupListComponent,
    RegisterForPeopleGroupComponent,
    SelectTestOriginComponent,
    SelectTestResultComponent,
    ExemptionStatusComponent,
  ],
  imports: [
    CommonModule,
    RegisterTestsRoutingModule,
    FormsModule,
    ...ANGULAR_MATERIAL_MODULES,
  ],
})
export class RegisterTestsModule {}

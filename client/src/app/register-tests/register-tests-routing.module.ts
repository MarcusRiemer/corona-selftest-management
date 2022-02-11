import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IndexComponent } from './index/index.component';
import { RegisterForPeopleGroupComponent } from './register-for-people-group/register-for-people-group.component';

const routes: Routes = [
  { path: '', component: IndexComponent },
  {
    path: 'group/:groupId/:date',
    component: RegisterForPeopleGroupComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class RegisterTestsRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AboutComponent } from './about/about.component';

const routes: Routes = [
  { path: '', component: AboutComponent, pathMatch: 'full' },
  {
    path: 'register-test',
    loadChildren: () =>
      import('./register-tests/register-tests.module').then(
        (m) => m.RegisterTestsModule
      ),
  },
  {
    path: 'analysis',
    loadChildren: () =>
      import('./analysis/analysis.module').then((m) => m.AnalysisModule),
  },
  {
    path: 'administration',
    loadChildren: () =>
      import('./administration/administration.module').then(
        (m) => m.AdministrationModule
      ),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

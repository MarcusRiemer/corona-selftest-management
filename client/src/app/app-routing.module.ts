import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
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
  {
    path: '',
    pathMatch: 'full',
    loadChildren: () => import('./core/core.module').then((m) => m.CoreModule),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

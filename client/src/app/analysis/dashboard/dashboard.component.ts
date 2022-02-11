import { Component } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import { CurrentDateService } from 'src/app/core/current-date.service';
import { DashboardDescription, DashboardService } from '../dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent {
  displayedColumns: (keyof DashboardDescription['grouped'][0])[] = [
    'name',
    'numNegative',
    'numPositive',
    'numLocal',
    'numThirdParty',
    'numSelfDisclosure',
    'numUnknown',
  ];

  constructor(
    private readonly dashboardService: DashboardService,
    private readonly _currentDate: CurrentDateService
  ) {}

  readonly data$: Observable<DashboardDescription> =
    this._currentDate.current$.pipe(
      switchMap((d) => this.dashboardService.fetchDashboard(d))
    );
}

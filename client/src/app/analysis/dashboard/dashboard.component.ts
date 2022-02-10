import { Component, Input, OnInit } from '@angular/core';
import { DashboardDescription, DashboardService } from '../dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  @Input()
  public data: DashboardDescription | undefined;

  displayedColumns: (keyof DashboardDescription['grouped'][0])[] = [
    'name',
    'numNegative',
    'numPositive',
    'numLocal',
    'numThirdParty',
    'numSelfDisclosure',
    'numUnknown',
  ];

  constructor(private readonly dashboardService: DashboardService) {}

  async ngOnInit() {
    this.data = await this.dashboardService.fetchDashboard();
  }
}

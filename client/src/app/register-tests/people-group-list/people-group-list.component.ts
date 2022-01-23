import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  GroupDataService,
  PeopleGroupListDescription,
} from 'src/app/core-services/group-data.service';
import { todayTestDate } from 'src/app/core-services/test-date';

@Component({
  selector: 'app-people-group-list',
  templateUrl: './people-group-list.component.html',
  styleUrls: ['./people-group-list.component.scss'],
})
export class PeopleGroupListComponent {
  @Input()
  group: PeopleGroupListDescription = {
    name: 'Laden ...',
    id: '-1',
    count: 0,
  };

  private readonly today = todayTestDate();

  constructor(
    private readonly groupData: GroupDataService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {}

  async createTestSeries() {
    if (await this.groupData.createGroupTestSeries(this.group.id, this.today)) {
      this.router.navigate([this.targetUrl], { relativeTo: this.route });
    } else {
      alert('Fehler beim Anlegen der Testreihe');
    }
  }

  get targetUrl() {
    return `group/${this.group.id}/${this.today}`;
  }
}

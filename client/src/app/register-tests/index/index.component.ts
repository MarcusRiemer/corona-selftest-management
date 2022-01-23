import { Component, OnInit } from '@angular/core';
import { GroupDataService, PeopleGroupListDescription } from 'src/app/core-services/group-data.service';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.scss'],
})
export class IndexComponent implements OnInit {
  constructor(private readonly _groupData : GroupDataService) {}

  async ngOnInit() {
    this.groups = await this._groupData.fetchAvailableGroups();
  }

  groups: PeopleGroupListDescription[] = [];
}

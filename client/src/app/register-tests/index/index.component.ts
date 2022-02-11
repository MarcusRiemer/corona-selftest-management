import { Component } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import {
  GroupDataService,
  PeopleGroupListDescription,
} from 'src/app/core-services/group-data.service';
import { CurrentDateService } from 'src/app/core/current-date.service';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.scss'],
})
export class IndexComponent {
  constructor(
    private readonly _groupData: GroupDataService,
    private readonly _currentDate: CurrentDateService
  ) {}

  readonly groups$: Observable<PeopleGroupListDescription[]> =
    this._currentDate.current$.pipe(
      switchMap((d) => this._groupData.fetchAvailableGroups(d))
    );
}

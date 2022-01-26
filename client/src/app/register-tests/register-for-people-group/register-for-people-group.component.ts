import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import {
  GroupDataService,
  TestResultsGroupListDescription,
  TestState,
} from 'src/app/core-services/group-data.service';
import {
  formatTestDate,
  parseTestDate,
  TestDate,
} from 'src/app/core-services/test-date';

@Component({
  selector: 'app-register-for-people-group',
  templateUrl: './register-for-people-group.component.html',
  styleUrls: ['./register-for-people-group.component.scss'],
})
export class RegisterForPeopleGroupComponent implements OnInit, OnDestroy {
  groupId: string = '-1';
  testDate: TestDate = formatTestDate('1970', '01', '01');

  data: TestResultsGroupListDescription = {
    date: this.testDate,
    group: {
      id: '-1',
      name: 'Missing Data',
      personCount: -1,
      testCount: -1,
    },
    results: [],
  };

  isLoading = true;

  private subscriptions: Subscription[] = [];

  constructor(
    private readonly route: ActivatedRoute,
    private readonly groupDate: GroupDataService
  ) {}

  ngOnInit(): void {
    const sub = this.route.paramMap.subscribe(async (params) => {
      this.isLoading = true;
      console.log('Register test result route params:', params);

      this.groupId = params.get('groupId') ?? '-1';
      this.testDate = parseTestDate(params);

      this.data = await this.groupDate.getGroupTestSeries(
        this.groupId,
        this.testDate
      );

      this.isLoading = false;
    });
    this.subscriptions = [sub];
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((s) => s.unsubscribe());
    this.subscriptions = [];
  }

  unknownToNegativeResult() {
    this.data.results
      .filter((r) => r.result.state.result === 'UNKNOWN')
      .forEach((r) => (r.result.state.result = 'NEGATIVE'));
  }

  unknownToOrigin(origin: TestState['origin']) {
    this.data.results
      .filter((r) => r.result.state.origin === 'UNKNOWN')
      .forEach((r) => (r.result.state.origin = origin));
  }

  deleteTestResult(personId: string) {
    this.data.results = this.data.results.filter(
      (r) => r.person.id !== personId
    );
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TestDate, toTestDate } from './test-date';
import { firstValueFrom } from 'rxjs';

export interface PersonDescription {
  id: string;
  firstName: string;
  lastName: string;
}

export interface PeopleGroupListDescription {
  id: string;
  name: string;
  personCount: number;
  testCount: number;
}

export interface TestState {
  origin: 'LOCAL' | 'THIRD_PARTY' | 'SELF_DISCLOSURE' | 'UNKNOWN';
  result: 'POSITIVE' | 'NEGATIVE' | 'UNKNOWN';
}

export interface TestResultDescription {
  id: string;
  state: TestState;
  date: TestDate;
}

export interface TestExemptionDescription {
  id: string;
  reason: 'VACCINATED' | 'RECOVERED' | 'ABSENT' | 'CUSTOM';
  comment: string;
  begin: TestDate;
  end: TestDate;
}

export interface PersonTestResultDescription {
  person: PersonDescription;
  result?: TestResultDescription;
  exemptions: TestExemptionDescription[];
}

export interface TestResultsGroupListDescription {
  date: TestDate;
  group: Omit<PeopleGroupListDescription, 'count'>;
  results: PersonTestResultDescription[];
}

export interface TestStateChangedParams {
  test: TestResultDescription;
  changedProp: keyof TestResultDescription['state'];
}

@Injectable({
  providedIn: 'root',
})
export class GroupDataService {
  constructor(private readonly http: HttpClient) {}

  async fetchAvailableGroups(
    date: Date
  ): Promise<PeopleGroupListDescription[]> {
    const formatted = toTestDate(date);
    console.log('Requesting groups for', formatted);
    return firstValueFrom(
      this.http.get<PeopleGroupListDescription[]>(
        `api/tests/groups?date=${formatted}`
      )
    );
  }

  async createGroupTestSeries(
    groupId: string,
    testDate: TestDate,
    testAll: boolean
  ): Promise<boolean> {
    return firstValueFrom(
      this.http.post<boolean>('api/tests/createForGroup', {
        groupId,
        testDate,
        testAll,
      })
    );
  }

  async getGroupTestSeries(
    groupId: string,
    date: TestDate
  ): Promise<TestResultsGroupListDescription> {
    return firstValueFrom(
      this.http.get<TestResultsGroupListDescription>(
        `api/tests/forGroup/${groupId}`
      )
    );
  }

  async setTestState(...changes: TestStateChangedParams[]) {
    const serverChanges = changes.map((c) => ({
      testId: c.test.id,
      changedProp: c.changedProp,
      value: c.test.state[c.changedProp],
    }));

    return firstValueFrom(
      this.http.post<boolean>(`api/tests/update`, serverChanges)
    );
  }

  async deleteTest(testId: string) {
    return firstValueFrom(this.http.delete<boolean>(`api/tests/${testId}`));
  }
}

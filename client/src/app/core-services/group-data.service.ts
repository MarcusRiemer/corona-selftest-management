import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TestDate } from './test-date';
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
  origin: 'SCHOOL' | 'THIRD_PARTY' | 'SELF_DISCLOSURE' | 'UNKNOWN';
  result: 'POSITIVE' | 'NEGATIVE' | 'UNKNOWN';
}

export interface TestResultDescription {
  id: string;
  state: TestState;
  date: TestDate;
}

export interface PersonTestResultDescription {
  person: PersonDescription;
  result: TestResultDescription;
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

  async fetchAvailableGroups(): Promise<PeopleGroupListDescription[]> {
    return firstValueFrom(
      this.http.get<PeopleGroupListDescription[]>('api/tests/groupsToday')
    );
  }

  async createGroupTestSeries(
    groupId: string,
    testDate: TestDate
  ): Promise<boolean> {
    return firstValueFrom(
      this.http.post<boolean>('api/tests/createForGroup', { groupId, testDate })
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

import { Injectable } from '@angular/core';
import { formatTestDate, TestDate } from './test-date';

export interface PersonDescription {
  id: string;
  firstName: string;
  lastName: string;
}

export interface PeopleGroupListDescription {
  id: string;
  name: string;
  count: number;
}

export interface TestState {
  origin: 'school' | 'thirdParty' | 'selfDisclosure' | 'unknown';
  result: 'positive' | 'negative' | 'unknown';
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

@Injectable({
  providedIn: 'root',
})
export class GroupDataService {
  constructor() {}

  async fetchAvailableGroups(): Promise<PeopleGroupListDescription[]> {
    return [
      {
        id: '1',
        name: '7a',
        count: 22,
      },
      {
        id: '2',
        name: '7b',
        count: 25,
      },
      {
        id: '3',
        name: '7c',
        count: 21,
      },
    ];
  }

  async createGroupTestSeries(
    groupId: string,
    date: TestDate
  ): Promise<boolean> {
    return true;
  }

  async getGroupTestSeries(
    groupId: string,
    date: TestDate
  ): Promise<TestResultsGroupListDescription> {
    return {
      date: date,
      group: {
        id: '1',
        name: '7a',
      },
      results: [
        {
          person: {
            id: '1',
            firstName: 'Arnold',
            lastName: 'Erster',
          },
          result: {
            id: '1001',
            state: { origin: 'unknown', result: 'unknown' },
            date,
          },
        },
        {
          person: {
            id: '2',
            firstName: 'Berta',
            lastName: 'Zweite',
          },
          result: {
            id: '1002',
            state: { origin: 'unknown', result: 'unknown' },
            date,
          },
        },
        {
          person: {
            id: '3',
            firstName: 'Cäsar',
            lastName: 'Dritter',
          },
          result: {
            id: '1003',
            state: { origin: 'unknown', result: 'unknown' },
            date,
          },
        },
      ],
    };
  }
}

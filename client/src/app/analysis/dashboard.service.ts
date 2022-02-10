import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { TestDate, todayTestDate } from '../core-services/test-date';

export interface CountDescription {
  numTests: number;
  numPositive: number;
  numNegative: number;
  numUnknown: number;
  numLocal: number;
  numThirdParty: number;
  numSelfDisclosure: number;
}

export interface GroupCountDescription extends CountDescription {
  name: string;
}

export interface DashboardDescription {
  overall: CountDescription;
  grouped: GroupCountDescription[];
}

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  constructor(private readonly http: HttpClient) {}

  async fetchDashboard(date?: TestDate): Promise<DashboardDescription> {
    date = date ?? todayTestDate();

    return firstValueFrom(
      this.http.get<DashboardDescription>('api/analysis/dashboard')
    );
  }
}

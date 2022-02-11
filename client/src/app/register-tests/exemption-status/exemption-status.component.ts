import { Component, Input } from '@angular/core';
import { TestExemptionDescription } from 'src/app/core-services/group-data.service';
import {
  fromTestDate,
  TestDate,
  todayTestDate,
} from 'src/app/core-services/test-date';

@Component({
  selector: 'app-exemption-status',
  templateUrl: './exemption-status.component.html',
  styleUrls: ['./exemption-status.component.scss'],
})
export class ExemptionStatusComponent {
  @Input()
  public exemptions: TestExemptionDescription[] = [];

  readonly now = new Date();

  isCurrent(exemption: TestExemptionDescription) {
    return false;
    // return this.now >= exemption.begin && this.now <= exemption.end;
  }

  asDate(d: TestDate): Date {
    return fromTestDate(d);
  }
}

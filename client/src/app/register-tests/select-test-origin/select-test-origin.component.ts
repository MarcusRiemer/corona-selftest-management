import { Component, Input } from '@angular/core';
import {
  GroupDataService,
  TestResultDescription,
} from 'src/app/core-services/group-data.service';
import { formatTestDate } from 'src/app/core-services/test-date';

@Component({
  selector: 'app-select-test-origin',
  templateUrl: './select-test-origin.component.html',
  styleUrls: ['./select-test-origin.component.scss'],
})
export class SelectTestOriginComponent {
  @Input()
  testResult: TestResultDescription = {
    id: '-1',
    state: { origin: 'UNKNOWN', result: 'UNKNOWN' },
    date: formatTestDate('1970', '01', '01'),
  };

  @Input()
  readOnly = false;

  updateInProgress = false;

  constructor(private readonly groupData: GroupDataService) {}

  async onChange() {
    if (!this.readOnly) {
      this.updateInProgress = true;
      await this.groupData.setTestState({
        test: this.testResult,
        changedProp: 'origin',
      });
      this.updateInProgress = false;
    }
  }
}

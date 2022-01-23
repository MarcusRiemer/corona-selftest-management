import { Component, Input, OnInit } from '@angular/core';
import { TestResultDescription } from 'src/app/core-services/group-data.service';
import { formatTestDate } from 'src/app/core-services/test-date';

@Component({
  selector: 'app-select-test-result',
  templateUrl: './select-test-result.component.html',
  styleUrls: ['./select-test-result.component.scss'],
})
export class SelectTestResultComponent implements OnInit {
  @Input()
  testResult: TestResultDescription = {
    id: '-1',
    state: { origin: 'unknown', result: 'unknown' },
    date: formatTestDate('1970', '01', '01'),
  };

  @Input()
  readOnly = false;

  constructor() {}

  ngOnInit(): void {}
}

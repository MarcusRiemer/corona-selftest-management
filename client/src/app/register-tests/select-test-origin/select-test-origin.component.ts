import { Component, Input, OnInit } from '@angular/core';
import {
  TestResultDescription,
  TestState,
} from 'src/app/core-services/group-data.service';
import { formatTestDate } from 'src/app/core-services/test-date';

@Component({
  selector: 'app-select-test-origin',
  templateUrl: './select-test-origin.component.html',
  styleUrls: ['./select-test-origin.component.scss'],
})
export class SelectTestOriginComponent implements OnInit {
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

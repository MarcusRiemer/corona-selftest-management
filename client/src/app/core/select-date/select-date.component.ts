import { Component, OnInit } from '@angular/core';
import { CurrentDateService } from '../current-date.service';

@Component({
  selector: 'app-select-date',
  templateUrl: './select-date.component.html',
  styleUrls: ['./select-date.component.scss'],
})
export class SelectDateComponent implements OnInit {
  constructor(readonly globalDate: CurrentDateService) {}

  ngOnInit(): void {}
}

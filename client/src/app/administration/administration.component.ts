import { Component } from '@angular/core';
import {
  PersonBatchImportService,
  UploadResult,
} from './person-batch-import.service';

@Component({
  selector: 'app-administration',
  templateUrl: './administration.component.html',
  styleUrls: ['./administration.component.scss'],
})
export class AdministrationComponent {
  constructor(private readonly personBatchImport: PersonBatchImportService) {}

  inProgress = false;

  lastUploadResult: UploadResult | undefined;

  async onImport(form: HTMLFormElement) {
    this.inProgress = true;
    const data = new FormData(form);
    this.lastUploadResult = await this.personBatchImport.uploadForm(data);
    this.inProgress = false;
  }
}

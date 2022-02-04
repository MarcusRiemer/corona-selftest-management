import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';

export interface UploadResult {
  numLines: number;
  errors: string[];
  skipped: string[];
  newGroups: string[];
}

@Injectable({
  providedIn: 'root',
})
export class PersonBatchImportService {
  constructor(private readonly http: HttpClient) {}

  async uploadForm(formData: FormData): Promise<UploadResult> {
    console.log('Starting batch person import', formData);

    return firstValueFrom(
      this.http.post<UploadResult>(
        `/api/administration/personBatchImport`,
        formData,
        { withCredentials: true }
      )
    );
  }
}

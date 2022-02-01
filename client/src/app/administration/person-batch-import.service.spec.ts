import { TestBed } from '@angular/core/testing';

import { PersonBatchImportService } from './person-batch-import.service';

describe('PersonBatchImportService', () => {
  let service: PersonBatchImportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PersonBatchImportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

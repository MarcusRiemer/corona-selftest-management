import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { toTestDate } from '../core-services/test-date';

@Injectable({
  providedIn: 'root',
})
export class CurrentDateService {
  private readonly _current$ = new BehaviorSubject(new Date());

  public readonly current$ = this._current$.asObservable();

  get current() {
    return this._current$.value;
  }

  get currentStringDate() {
    return toTestDate(this.current);
  }

  set current(d: Date) {
    console.log(`Current date set to "${toTestDate(d)}", or precise: `, d);
    this._current$.next(d);
  }
}

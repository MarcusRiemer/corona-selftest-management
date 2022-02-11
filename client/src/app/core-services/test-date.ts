import { ParamMap } from '@angular/router';

export type TestDay =
  | '01'
  | '02'
  | '03'
  | '04'
  | '05'
  | '06'
  | '07'
  | '08'
  | '09'
  | '10'
  | '11'
  | '12'
  | '13'
  | '14'
  | '15'
  | '16'
  | '17'
  | '18'
  | '19'
  | '20'
  | '21'
  | '22'
  | '23'
  | '24'
  | '25'
  | '26'
  | '27'
  | '28'
  | '29'
  | '30'
  | '31';

export type TestMonth =
  | '01'
  | '02'
  | '03'
  | '04'
  | '05'
  | '06'
  | '07'
  | '08'
  | '09'
  | '10'
  | '11'
  | '12';

export type TestYear = `${number}`;

export type TestDate = `${TestYear}-${TestMonth}-${TestDay}`;

const isMonth = (m: string | number): m is TestMonth => {
  const i = typeof m === 'string' ? parseInt(m, 10) : m;
  return i >= 1 && i <= 12;
};

const isDay = (d: string | number): d is TestDay => {
  const i = typeof d === 'string' ? parseInt(d, 10) : d;
  return i >= 1 && i <= 31;
};

export const formatTestDate = (
  year: TestYear,
  month: TestMonth,
  day: TestDay
): TestDate => {
  return `${year}-${month}-${day}`;
};

export const parseTestDate = (params: ParamMap): TestDate => {
  const whole = params.get('date');
  if (!whole) {
    throw new Error(`No "date" in URL: ${JSON.stringify(params)}`);
  }
  const split = whole.split('-');
  const year = split[0];
  const month = split[1];
  const day = split[2];
  if (!year || !month || !day) {
    throw new Error(`Invalid date: "${year}"-"${month}"-"${day}`);
  }

  if (isMonth(month) && isDay(day)) {
    return formatTestDate(`${+year}`, month, day);
  } else {
    throw new Error(`Not valid date parameters: ${JSON.stringify(params)}`);
  }
};

export const toTestDate = (date: Date): TestDate => {
  const year = date.getFullYear();
  const month = ('' + (date.getMonth() + 1)).padStart(2, '0');
  const day = ('' + date.getDate()).padStart(2, '0');
  if (isMonth(month) && isDay(day)) {
    return formatTestDate(`${year}`, month, day);
  } else {
    throw new Error(`Not valid date object: ${JSON.stringify(date)}`);
  }
};

export const fromTestDate = (d: TestDate): Date => {
  return new Date(Date.parse(d));
};

export const todayTestDate = (): TestDate => {
  return toTestDate(new Date());
};

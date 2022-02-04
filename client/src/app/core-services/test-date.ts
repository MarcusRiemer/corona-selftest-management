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

export type TestDate = `${TestYear}/${TestMonth}/${TestDay}`;

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
  return `${year}/${month}/${day}`;
};

export const parseTestDate = (params: ParamMap): TestDate => {
  const year = params.get('year');
  const month = params.get('month')?.padStart(2, '0');
  const day = params.get('day')?.padStart(2, '0');
  if (!year || !month || !day) {
    throw new Error(`Invalid date: "${year}"/"${month}"/"${day}`);
  }

  if (isMonth(month) && isDay(day)) {
    return formatTestDate(`${+year}`, month, day);
  } else {
    throw new Error(`Not valid date parameters: ${JSON.stringify(params)}`);
  }
};

export const toTestDate = (date: Date): TestDate => {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  if (isMonth(month) && isDay(day)) {
    return formatTestDate(`${year}`, month, day);
  } else {
    throw new Error(`Not valid date object: ${JSON.stringify(date)}`);
  }
};

export const todayTestDate = (): TestDate => {
  return toTestDate(new Date());
};

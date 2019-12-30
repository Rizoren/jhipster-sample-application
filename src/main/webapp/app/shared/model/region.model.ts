export interface IRegion {
  id?: number;
  regionName?: string;
  regionCode?: string;
}

export class Region implements IRegion {
  constructor(public id?: number, public regionName?: string, public regionCode?: string) {}
}

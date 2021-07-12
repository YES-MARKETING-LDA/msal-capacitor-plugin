import { WebPlugin } from '@capacitor/core';

import type { MsalCapacitorPlugin } from './definitions';

export class MsalCapacitorWeb extends WebPlugin implements MsalCapacitorPlugin {

  /*constructor() {
    super({
      name: 'MsalCapacitorPlugin',
      platforms: ['web']
    });
  }*/

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async loginWithMsAD(options: any): Promise<any> {
    console.log('ECHO', options);
    return options;
  }

}

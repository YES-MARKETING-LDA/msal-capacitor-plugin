import { WebPlugin } from '@capacitor/core';

import type { MsalCapacitorPlugin } from './definitions';

export class MsalCapacitorWeb extends WebPlugin implements MsalCapacitorPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

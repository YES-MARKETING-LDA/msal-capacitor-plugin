import { registerPlugin } from '@capacitor/core';

import type { MsalCapacitorPlugin } from './definitions';

const MsalCapacitor = registerPlugin<MsalCapacitorPlugin>('MsalCapacitor', {
  web: () => import('./web').then(m => new m.MsalCapacitorWeb()),
});

export * from './definitions';
export { MsalCapacitor };

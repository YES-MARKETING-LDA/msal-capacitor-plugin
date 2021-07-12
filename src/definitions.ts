export interface MsalCapacitorPlugin {

  echo(options: { value: string }): Promise<{ value: string }>;

  loginWithMsAD(options: any): Promise<any>;

}

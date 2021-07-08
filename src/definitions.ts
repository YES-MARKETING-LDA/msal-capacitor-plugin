export interface MsalCapacitorPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}

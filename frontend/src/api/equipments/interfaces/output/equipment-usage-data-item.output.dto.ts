export default interface EquipmentUsageDataItemOutputDto {
  usageStart: Date;
  usageEnd: Date;
  operatingHours: number;
  energyConsumed: number;
  loadOrCapacityUsed: number;
  location: string;
  operator: string;
  notes: string;
  date: Date;
  id: string;
}

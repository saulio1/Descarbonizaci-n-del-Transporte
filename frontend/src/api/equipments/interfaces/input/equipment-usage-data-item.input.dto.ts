export default interface EquipmentUsageDataItemInputDto{
  usageStart: Date;
  usageEnd: Date;
  operatingHours: number;
  energyConsumed: number;
  loadOrCapacityUsed: number;
  location: string;
  operator: string;
  notes: string;
  id: string;
}

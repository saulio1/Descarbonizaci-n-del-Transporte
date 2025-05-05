import type WorkerPositionOutputDto from '@/api/worker-positions/interfaces/output/worker-position.output.dto.ts'

export default interface WorkerOutputDto {
  id: string
  names: string
  lastNames: string
  cid: string
  phoneNumber: string
  email: string
  position: WorkerPositionOutputDto
}

import type { PageDomain, BaseEntity } from "../common";

export interface ResponsibleElder extends BaseEntity {
    id?: number;
    elderId?: number;
    elderName?: string;
    floorId?: number;
    floorName?: string;
    roomId?: number;
    roomCode?: string;
    bedId?: number;
    bedNumber?: string;
    status?: string;
}
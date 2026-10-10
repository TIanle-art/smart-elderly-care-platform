import type { PageDomain, BaseEntity } from "../common";

/**
 * 老人信息（对应后端 Elder）
 */
export interface Elder {
    id: number;
    name: string;
    idCardNo?: string;
    phone?: string;
    status?: number;

}
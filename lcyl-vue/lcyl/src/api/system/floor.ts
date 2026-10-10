import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, FloorQueryParams, Floor } from '@/types'

// 获取楼层房间床位
export function getFloorRoomBed(floorId: number) {
  return request({
    url: '/system/bed/floor/' + floorId,
    method: 'get'
  })
}
// 查询楼层表
export function listFloor(query: FloorQueryParams): Promise<TableDataInfo<Floor[]>> {
  return request({
    url: '/system/floor/list',
    method: 'get',
    params: query
  })
}

// 查询房楼层表详细
export function getFloor(id: number): Promise<AjaxResult<Floor>> {
  return request({
    url: '/system/floor/' + id,
    method: 'get'
  })
}

// 新增楼层表
export function addFloor(data: Floor): Promise<AjaxResult> {
  return request({
    url: '/system/floor',
    method: 'post',
    data: data
  })
}

// 修改楼层表
export function updateFloor(data: Floor): Promise<AjaxResult> {
  return request({
    url: '/system/floor',
    method: 'put',
    data: data
  })
}

// 删除楼层表
export function delFloor(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/system/floor/' + id,
    method: 'delete'
  })
}
import {Bed, BedQueryParams} from "@/types/api/system/bed";

// 查询床位表列表
export function listBed(query: BedQueryParams): Promise<TableDataInfo<Bed[]>> {
  return request({
    url: '/system/bed/list',
    method: 'get',
    params: query
  })
}

// 查询床位表详细
export function getBed(id: number): Promise<AjaxResult<Bed>> {
  return request({
    url: '/system/bed/' + id,
    method: 'get'
  })
}

// 新增床位表
export function addBed(data: Bed): Promise<AjaxResult> {
  return request({
    url: '/system/bed',
    method: 'post',
    data: data
  })
}

// 修改床位表
export function updateBed(data: Bed): Promise<AjaxResult> {
  return request({
    url: '/system/bed',
    method: 'put',
    data: data
  })
}

// 删除床位表
// 删除床位（单个/批量都支持）
export function deleteBed(ids: number[]) {
  return request({
    url: '/system/bed/' + ids, // 关键修复
    method: 'delete'
  })
}

import {Room, RoomQueryParams} from "@/types/api/system/room";

// 查询房间表列表
export function listRoom(query: RoomQueryParams): Promise<TableDataInfo<Room[]>> {
  return request({
    url: '/system/room/list',
    method: 'get',
    params: query
  })
}

// 查询房间表详细
export function getRoom(id: number): Promise<AjaxResult<Room>> {
  return request({
    url: '/system/room/' + id,
    method: 'get'
  })
}

// 新增房间表
export function addRoom(data: Room): Promise<AjaxResult> {
  return request({
    url: '/system/room',
    method: 'post',
    data: data
  })
}

// 修改房间表
export function updateRoom(data: Room): Promise<AjaxResult> {
  return request({
    url: '/system/room',
    method: 'put',
    data: data
  })
}

// 删除房间表
export function delRoom(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/system/room/' + id,
    method: 'delete'
  })
}








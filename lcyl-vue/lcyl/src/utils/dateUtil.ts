/**
 * 日期格式化工具（TypeScript版）
 * @param date 日期（字符串/时间戳/Date对象）
 * @param fmt 格式 默认 yyyy-MM-dd HH:mm:ss
 * @returns 格式化后的日期字符串
 */
export function formatDate(date: string | number | Date, fmt = 'yyyy-MM-dd HH:mm:ss'): string {
    if (!date) return '';

    // 统一转为Date对象
    const _date = new Date(date);
    if (isNaN(_date.getTime())) return '';

    const o: Record<string, number> = {
        'M+': _date.getMonth() + 1,                 // 月份
        'd+': _date.getDate(),                    // 日
        'H+': _date.getHours(),                   // 小时
        'm+': _date.getMinutes(),                 // 分
        's+': _date.getSeconds(),                 // 秒
        'q+': Math.floor((_date.getMonth() + 3) / 3), // 季度
        'S': _date.getMilliseconds()             // 毫秒
    };

    // 处理年份
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (_date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }

    // 处理其他格式
    for (const k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            fmt = fmt.replace(
                RegExp.$1,
                RegExp.$1.length === 1 ? `${o[k]}` : (`00${o[k]}`).substr((`${o[k]}`).length)
            );
        }
    }

    return fmt;
}
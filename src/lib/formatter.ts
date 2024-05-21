export const convertTime = (second: number) => {
  const hours = Math.floor(second / 3600);
  const minutes = Math.floor((second % 3600) / 60);
  const seconds = Math.floor(second % 60);
  if (hours > 0) {
    return `${hours}시간 ${minutes}분`
  }
  if (minutes === 0) {
    return `${seconds}초`
  }
  if (seconds === 0) {
    return `${minutes}분`
  }
  return `${minutes}분 ${seconds}초`;
}

export const convertDistance = (distance: number) => {
  const kilo = distance / 1000;
  if (kilo > 0) {
    return `${kilo.toFixed(2)}km`
  }
  return `${distance}m`;
}

export const toDate = (date: Date) => {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  return `${year}-${month}-${day}`
}

export const toDateTime = (date: Date) => {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  const hour = date.getHours().toString().padStart(2, '0');
  const minute = date.getMinutes().toString().padStart(2, '0');
  return `${year}-${month}-${day}T${hour}:${minute}`
}

export const toTime = (date: Date) => {
  const hour = date.getHours().toString().padStart(2, '0');
  const minute = date.getMinutes().toString().padStart(2, '0');
  return `${hour}:${minute}`
}
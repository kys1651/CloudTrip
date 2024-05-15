export interface Point {
  latitude: number;
  longitude: number;
  tourId: number;
}

function toRadians(degrees: number): number {
  return degrees * Math.PI / 180;
}

export const calculateDistance = (pointA: Point, pointB: Point): number => {
  const earthRadiusKm = 6371;

  const dLat = toRadians(pointB.latitude - pointA.latitude);
  const dLon = toRadians(pointB.longitude - pointA.longitude);

  const lat1 = toRadians(pointA.latitude);
  const lat2 = toRadians(pointB.latitude);

  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  return earthRadiusKm * c;
}

export function calculateSum(points: Point[]): number {
  return points.reduce((sum, point, index) => {
    if (index === 0) return sum;
    return sum + calculateDistance(points[index - 1], point);
  }, 0);
}
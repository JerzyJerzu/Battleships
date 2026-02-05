/**
 * Backend error response format.
 * Returned when API calls fail (404, 500, etc.)
 */
export interface ErrorResponse {
	error: string;
}

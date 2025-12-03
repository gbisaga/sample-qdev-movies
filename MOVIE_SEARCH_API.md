# 🏴‍☠️ Movie Search API Documentation - Pirate's Treasure Hunt Guide

Ahoy matey! Welcome to the Movie Search API documentation. This guide will help ye navigate the treacherous waters of movie searching like a true pirate captain!

## 🗺️ Overview

The Movie Search API allows ye to search through our treasure chest of movies using various criteria. Whether ye be looking for a specific movie by name, hunting for movies of a particular genre, or seeking a movie by its unique ID, this API has got ye covered!

## ⚓ Endpoints

### 1. HTML Search Endpoint (For Web Browsers)

**Endpoint:** `GET /movies/search`

**Description:** Arrr! This endpoint be perfect for landlubbers using web browsers. It returns a beautiful HTML page with search results and a search form.

**Parameters:**
- `name` (optional): Movie name to search for (partial matching, case-insensitive)
- `id` (optional): Exact movie ID to find
- `genre` (optional): Genre to filter by (case-insensitive)

**Example Requests:**
```
GET /movies/search?name=pirate
GET /movies/search?genre=adventure
GET /movies/search?id=1
GET /movies/search?name=treasure&genre=adventure
```

**Response:** Returns HTML page with search results and pirate-themed messages.

### 2. JSON API Search Endpoint (For Other Ships/Applications)

**Endpoint:** `GET /movies/search/api`

**Description:** Yo ho ho! This endpoint be for other applications that want to communicate with our movie service using JSON. Perfect for building yer own pirate applications!

**Parameters:**
- `name` (optional): Movie name to search for (partial matching, case-insensitive)
- `id` (optional): Exact movie ID to find
- `genre` (optional): Genre to filter by (case-insensitive)

**Example Requests:**
```
GET /movies/search/api?name=pirate
GET /movies/search/api?genre=adventure
GET /movies/search/api?id=1
GET /movies/search/api?name=treasure&genre=adventure
```

**Response Format:**
```json
{
  "success": true,
  "message": "Found 2 movies in our treasure chest!",
  "movies": [
    {
      "id": 1,
      "movieName": "The Pirate's Treasure",
      "director": "Captain Hook",
      "year": 2023,
      "genre": "Adventure",
      "description": "A pirate's quest for gold",
      "duration": 120,
      "imdbRating": 4.5,
      "icon": "🎬"
    }
  ],
  "totalResults": 1,
  "searchParameters": {
    "name": "pirate",
    "id": null,
    "genre": null
  }
}
```

## 🎯 Search Behavior

### Name Search
- **Case-insensitive:** "PIRATE", "pirate", and "PiRaTe" all work the same
- **Partial matching:** Searching for "The" will find "The Pirate's Treasure", "The Sea Battle", etc.
- **Whitespace handling:** Leading and trailing spaces are automatically trimmed

### Genre Search
- **Case-insensitive:** "ADVENTURE", "adventure", and "Adventure" all work
- **Partial matching:** Searching for "Drama" will find "Crime/Drama" movies
- **Multiple genres:** Movies with combined genres like "Action/Sci-Fi" can be found by searching for either "Action" or "Sci-Fi"

### ID Search
- **Exact matching:** Only finds the movie with the exact ID
- **Validation:** Negative IDs and zero are rejected
- **Priority:** When ID is provided, it takes priority over other search criteria

### Combined Search
When multiple parameters are provided, they work together (AND logic):
- `name=pirate&genre=adventure` finds movies that contain "pirate" in the name AND have "adventure" in the genre

## 🚨 Error Handling

### No Search Parameters
**Request:** `GET /movies/search/api`
**Response:**
```json
{
  "success": false,
  "message": "Arrr! At least one search parameter be required, matey!",
  "movies": [],
  "totalResults": 0
}
```
**HTTP Status:** 400 Bad Request

### No Results Found
**Request:** `GET /movies/search/api?name=nonexistent`
**Response:**
```json
{
  "success": true,
  "message": "No movies found matching yer search criteria, ye scurvy dog!",
  "movies": [],
  "totalResults": 0
}
```
**HTTP Status:** 200 OK

### Server Error
**Response:**
```json
{
  "success": false,
  "message": "Arrr! Something went wrong with the search. Try again later, me hearty!",
  "movies": [],
  "totalResults": 0
}
```
**HTTP Status:** 500 Internal Server Error

## 🎬 Available Movies

Our treasure chest currently contains these movie genres:
- Action
- Adventure
- Crime/Drama
- Drama
- Drama/History
- Drama/Romance
- Drama/Thriller
- Action/Crime
- Action/Sci-Fi
- Adventure/Fantasy
- Adventure/Sci-Fi

## 📝 Usage Examples

### Example 1: Search by Movie Name
```bash
curl "http://localhost:8080/movies/search/api?name=prison"
```

### Example 2: Search by Genre
```bash
curl "http://localhost:8080/movies/search/api?genre=drama"
```

### Example 3: Search by ID
```bash
curl "http://localhost:8080/movies/search/api?id=1"
```

### Example 4: Combined Search
```bash
curl "http://localhost:8080/movies/search/api?name=the&genre=drama"
```

### Example 5: HTML Search Form
Navigate to: `http://localhost:8080/movies` and use the search form at the top of the page.

## 🔧 Integration Tips

### For Frontend Developers
1. Use the HTML endpoint (`/movies/search`) for direct browser navigation
2. Use the API endpoint (`/movies/search/api`) for AJAX calls
3. Always check the `success` field in API responses
4. Display the `message` field to users for feedback

### For Backend Developers
1. The API returns consistent JSON structure for all responses
2. HTTP status codes follow REST conventions
3. Search parameters are validated server-side
4. All text searches are case-insensitive by default

### Error Handling Best Practices
```javascript
fetch('/movies/search/api?name=pirate')
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      console.log(`Found ${data.totalResults} movies!`);
      displayMovies(data.movies);
    } else {
      console.error('Search failed:', data.message);
      showErrorMessage(data.message);
    }
  })
  .catch(error => {
    console.error('Network error:', error);
    showErrorMessage('Arrr! Something went wrong with the search, matey!');
  });
```

## 🏴‍☠️ Pirate Language Guide

Our API uses authentic pirate language in messages. Here's a quick translation guide:

- **"Ahoy!"** = Hello/Greeting
- **"Arrr!"** = Expression of frustration or emphasis
- **"Matey/Me hearty"** = Friend/Buddy
- **"Ye/Yer"** = You/Your
- **"Scurvy dog/Landlubber"** = Playful insult
- **"Shiver me timbers!"** = Expression of surprise
- **"Yo ho ho!"** = Expression of joy/success
- **"Batten down the hatches!"** = Prepare for trouble
- **"Treasure chest"** = Our movie database

## 🚀 Performance Notes

- Search operations are performed in-memory for fast response times
- All movies are loaded at application startup
- Case-insensitive searches use lowercase conversion for efficiency
- No pagination is currently implemented (all results are returned)

## 🔮 Future Enhancements

Planned features for future releases:
- Pagination support for large result sets
- Advanced filtering (by year, rating, duration)
- Sorting options (by name, year, rating)
- Full-text search in movie descriptions
- Search result caching
- Rate limiting for API endpoints

---

**Remember:** Always provide at least one search parameter, or ye'll be walking the plank! 🏴‍☠️

For more information or to report issues, contact the crew at our GitHub repository.
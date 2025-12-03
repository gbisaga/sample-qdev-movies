# 🏴‍☠️ Movie Service - Pirate's Treasure Chest of Cinema

Ahoy matey! Welcome to the Movie Service - a swashbuckling Spring Boot demo application that serves as yer treasure chest of cinematic adventures! This application demonstrates Java development best practices with a pirate's flair.

## ⚓ Features

- **🎬 Movie Catalog**: Browse 12 classic movies with detailed information
- **🔍 Movie Search & Filtering**: Search for movies by name, ID, or genre with our powerful search functionality
- **📋 Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **⭐ Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **📱 Responsive Design**: Mobile-first design that works on all devices like a ship that sails on any sea
- **🎨 Modern UI**: Dark theme with gradient backgrounds and smooth animations
- **🏴‍☠️ Pirate Theme**: Enjoy the adventure with authentic pirate language throughout the application

## 🛠️ Technology Stack

- **Java 8** - The foundation of our ship
- **Spring Boot 2.7.18** - Our trusty navigation system
- **Maven** - For managing our treasure (dependencies)
- **Thymeleaf** - For crafting beautiful HTML templates
- **Log4j 2** - For keeping a ship's log
- **JUnit 5.8.2** - For testing our crew's work

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher (ye need a proper compass!)
- Maven 3.6+ (for managing yer treasure)

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080` - ready to set sail!

### Access the Application

- **🏠 Movie List**: http://localhost:8080/movies
- **🔍 Movie Search**: Use the search form on the main page or directly access http://localhost:8080/movies/search
- **📖 Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)

## 🔍 Search & Filtering Features

### HTML Search Interface
Navigate to the main movies page and use the pirate-themed search form:
- **Movie Name**: Search by partial movie name (case-insensitive)
- **Movie ID**: Find a specific movie by its unique ID
- **Genre**: Filter movies by genre using the dropdown

### API Search Endpoints
For developers building their own applications:

```bash
# Search by name
curl "http://localhost:8080/movies/search/api?name=prison"

# Search by genre
curl "http://localhost:8080/movies/search/api?genre=drama"

# Search by ID
curl "http://localhost:8080/movies/search/api?id=1"

# Combined search
curl "http://localhost:8080/movies/search/api?name=the&genre=drama"
```

## 🏗️ Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller with search endpoints
│   │       │   ├── MovieService.java         # Business logic with search functionality
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review business logic
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie data treasure chest
│       ├── mock-reviews.json                 # Mock review data
│       ├── templates/                        # Thymeleaf HTML templates
│       │   ├── movies.html                   # Main page with search form
│       │   └── movie-details.html            # Movie details page
│       ├── static/css/                       # Pirate-themed stylesheets
│       └── log4j2.xml                        # Logging configuration
└── test/                                     # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MovieServiceSearchTest.java   # Search functionality tests
            ├── MoviesControllerSearchTest.java # Controller tests
            └── MoviesControllerTest.java     # Original controller tests
```

## 🗺️ API Endpoints

### Get All Movies
```
GET /movies
```
Returns an HTML page displaying all movies with search functionality and pirate-themed interface.

### Search Movies (HTML)
```
GET /movies/search?name={name}&id={id}&genre={genre}
```
Returns an HTML page with filtered movie results based on search criteria.

**Parameters:**
- `name` (optional): Movie name to search for (partial matching, case-insensitive)
- `id` (optional): Exact movie ID to find
- `genre` (optional): Genre to filter by (case-insensitive)

### Search Movies (JSON API)
```
GET /movies/search/api?name={name}&id={id}&genre={genre}
```
Returns JSON response with search results - perfect for building yer own applications!

**Response Format:**
```json
{
  "success": true,
  "message": "Found 2 movies in our treasure chest!",
  "movies": [...],
  "totalResults": 2,
  "searchParameters": {
    "name": "pirate",
    "id": null,
    "genre": null
  }
}
```

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

## 🧪 Testing

Run the comprehensive test suite:

```bash
# Run all tests
mvn test

# Run specific test classes
mvn test -Dtest=MovieServiceSearchTest
mvn test -Dtest=MoviesControllerSearchTest
```

Our test suite includes:
- **MovieServiceSearchTest**: Tests for all search functionality with pirate-themed test names
- **MoviesControllerSearchTest**: Tests for search endpoints (both HTML and API)
- **MoviesControllerTest**: Original controller functionality tests

## 🔧 Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

1. Verify at least one search parameter is provided
2. Check the application logs for any errors
3. Ensure the movies.json file is properly loaded

## 📚 Documentation

- **[Movie Search API Documentation](MOVIE_SEARCH_API.md)** - Comprehensive guide to the search functionality
- **JavaDoc**: Generate with `mvn javadoc:javadoc`

## 🎯 Usage Examples

### Basic Search Examples

1. **Search by movie name:**
   - Navigate to http://localhost:8080/movies
   - Enter "Prison" in the movie name field
   - Click "Search for Treasure!"

2. **Filter by genre:**
   - Select "Drama" from the genre dropdown
   - Click "Search for Treasure!"

3. **Find specific movie:**
   - Enter "1" in the Movie ID field
   - Click "Search for Treasure!"

### API Integration Examples

```javascript
// Search for movies using fetch API
fetch('/movies/search/api?name=pirate&genre=adventure')
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      console.log(`Yo ho ho! Found ${data.totalResults} movies!`);
      displayMovies(data.movies);
    } else {
      console.error('Search failed:', data.message);
    }
  });
```

## 🏴‍☠️ Contributing

This project welcomes contributions from fellow pirates! Feel free to:
- Add more movies to the treasure chest
- Enhance the search functionality
- Improve the pirate-themed UI/UX
- Add new features like advanced filtering
- Improve the responsive design
- Add more comprehensive tests

### Development Guidelines
- Follow the existing pirate theme in comments and messages
- Write comprehensive tests for new functionality
- Use proper Java naming conventions
- Include JavaDoc comments for public methods
- Maintain the existing code style and structure

## 🎨 Customization

### Adding New Movies
Edit `src/main/resources/movies.json` to add new movies to the treasure chest.

### Customizing the Pirate Theme
- Modify CSS in `src/main/resources/static/css/movies.css`
- Update HTML templates in `src/main/resources/templates/`
- Adjust pirate language in controller and service classes

### Configuration
Modify `src/main/resources/application.yml` for application settings.

## 📄 License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

**Remember, matey:** The sea be vast and full of treasures. Use this application as a starting point for yer own cinematic adventures! 🏴‍☠️⚓🎬

# Property Management System

A comprehensive 3-tier enterprise application for managing real estate properties, built with modern Jakarta EE technologies.

## 🏗️ Architecture

This system follows a classic 3-tier architecture pattern:

### Presentation Tier
- **Technology**: JavaServer Faces (JSF) with XHTML views
- **Features**: User authentication, property search, management interfaces
- **Components**: Managed Beans, Facelets templates, CSS styling

### Business Tier
- **Technology**: Enterprise JavaBeans (EJB) with stateless session beans
- **Features**: Transaction management, business logic, validation
- **Components**: PropertyEJB, PropertyManagerEJB, UserEJB, AllocationEJB

### Persistence Tier
- **Technology**: Jakarta Persistence API (JPA) with EclipseLink
- **Database**: MySQL with relational schema
- **Features**: ORM mapping, entity relationships, data validation

## 🚀 Features

### Property Management
- **Property Types**: Support for both rental and sale properties
- **Search & Filter**: Advanced search by location, price, bedrooms, etc.
- **CRUD Operations**: Complete create, read, update, delete functionality
- **Address Management**: Integrated address system with validation

### User Management
- **Authentication**: Secure login system with password hashing
- **Registration**: User signup with email verification
- **Password Recovery**: Email-based password reset functionality
- **Session Management**: Secure session handling with filters

### Property Manager System
- **Manager Profiles**: Comprehensive property manager information
- **Property Allocation**: Assign properties to managers
- **Search Capabilities**: Find managers by name, email, or properties
- **Performance Tracking**: Monitor manager workload and assignments

## 🛠️ Technology Stack

- **Core Language**: Java 17+
- **Enterprise Framework**: Jakarta EE 10
- **Web Framework**: JavaServer Faces (JSF) 4.0
- **Persistence**: Jakarta Persistence API (JPA) 3.0
- **Business Logic**: Enterprise JavaBeans (EJB) 4.0
- **Database**: MySQL 8.0+
- **Build Tool**: Apache Maven
- **Application Server**: GlassFish/Payara
- **ORM Provider**: EclipseLink

## 📊 Database Schema

### Core Entities
- **Property**: Base entity with inheritance (RentProperty, SaleProperty)
- **PropertyManager**: Property manager information and assignments
- **User**: System users with authentication
- **Allocation**: Property-to-manager assignments
- **Address**: Comprehensive address management

### Relationships
- One-to-Many: PropertyManager ↔ Allocation
- Many-to-One: Allocation ↔ Property, Allocation ↔ PropertyManager
- One-to-One: Property ↔ Address
- Inheritance: Property → RentProperty, SaleProperty

## 🚦 Getting Started

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Jakarta EE compatible application server (GlassFish/Payara recommended)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Property-Management-System
   ```

2. **Database Setup**
   ```sql
   CREATE DATABASE real_estate_management;
   CREATE USER 'realestate'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON real_estate_management.* TO 'realestate'@'localhost';
   ```

3. **Configure Database Connection**
   - Update `src/conf/persistence.xml` with your database credentials
   - Configure JNDI datasource in your application server

4. **Build and Deploy**
   ```bash
   mvn clean install
   ```
   - Deploy the resulting WAR file to your application server

5. **Access the Application**
   - Open browser to: `http://localhost:8080/Property-Management-System/`
   - Default login: Register a new account or contact administrator

## 🔧 Configuration

### Database Configuration
Update `src/conf/persistence.xml`:
```xml
<jta-data-source>jdbc/RealEstateManagement</jta-data-source>
```

### Application Server Settings
- Configure JNDI datasource: `jdbc/RealEstateManagement`
- Set up mail session for email functionality
- Configure security realms if needed

## 📱 Application Features

### User Interface
- **Dashboard**: Overview of properties and managers
- **Property Listings**: Browse and search properties
- **Manager Portal**: Property manager dashboard and assignments
- **Admin Panel**: System administration and user management

### Search Capabilities
- Property search by location, price range, bedrooms/bathrooms
- Manager search by name, email, or property count
- Advanced filtering for furnished/unfurnished rentals
- Property type filtering (House, Apartment, Townhouse, etc.)

### Security Features
- SHA-256 password hashing with salt
- Session-based authentication
- Role-based access control
- Email verification for registration
- Secure password recovery

## 🧪 Testing

The application includes comprehensive validation and error handling:
- Input validation on all forms
- Database constraint enforcement
- Transaction rollback on errors
- User-friendly error messages

## 📈 Performance

- **Connection Pooling**: Efficient database connection management
- **Lazy Loading**: Optimized entity relationship loading
- **Caching**: JPA second-level cache support
- **Transaction Management**: Optimized transaction boundaries

## 🔍 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── realestatemanagement/
│   │       ├── model/          # JPA Entities
│   │       ├── ejb/            # Business Logic
│   │       ├── Beans/          # JSF Managed Beans
│   │       └── jsf/            # JSF Components
│   └── resources/
│       └── conf/
│           └── persistence.xml # JPA Configuration
└── web/
    ├── WEB-INF/
    │   └── web.xml            # Web Application Configuration
    └── *.xhtml                # JSF Views
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For questions or support:
- Create an issue in the repository
- Contact the development team
- Check the documentation for common issues

---

**Built with ❤️ using Jakarta EE technologies**

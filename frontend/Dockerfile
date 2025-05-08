# Use an official Node.js image as a parent image
FROM node:18-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy package.json and install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the React application
RUN npm run build

#-----

# Use an Nginx image to serve the built files
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
#COPY ./nginx.conf /etc/nginx/nginx.conf

# Expose the port Nginx runs on
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
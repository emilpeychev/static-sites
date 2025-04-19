FROM php:apache

# Add the user unprivileged_user
RUN apt-get update && apt-get install -y adduser
RUN adduser --disabled-password --gecos '' unprivileged_user


# Copy files into the web root
COPY ./website /var/www/html

# Set permissions 
RUN chmod -R 755 /var/www/html \
 && chown -R unprivileged_user:unprivileged_user /var/www/html

# Use port 80
EXPOSE 80

# Switch to the non-root user
USER unprivileged_user

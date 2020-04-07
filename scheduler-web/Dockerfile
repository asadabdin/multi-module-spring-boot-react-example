FROM node:carbon
WORKDIR /app
COPY package*.json ./
RUN npm install --silent
COPY . .
EXPOSE 4200 4200
CMD ["npm", "start"]